package id.del.ac.delstat.presentation.chat.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityCreateChatRoomBinding
import id.del.ac.delstat.presentation.chat.adapter.UserChatRoomAdapter
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModel
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModelFactory
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import id.del.ac.delstat.util.Helper
import id.del.ac.delstat.util.SubsamplingScaleImageViewTarget
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class CreateChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateChatRoomBinding

    @Inject
    lateinit var chatViewModelFactory: ChatViewModelFactory
    private lateinit var chatViewModel: ChatViewModel

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var userViewModel: UserViewModel

    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    lateinit var userChatRoomAdapter: UserChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatViewModel = ViewModelProvider(this, chatViewModelFactory)
            .get(ChatViewModel::class.java)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        prepareUI()
        initRecyclerView()
        getUserChatRoom()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        // Setting duration for animation when enlarging Image Profile
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        userChatRoomAdapter = UserChatRoomAdapter(
            clickListener = { user: User ->
                onClickUserChatRoom(user)
            },
            imageProfileClickListener = { imageViewProfile: ImageView, fotoProfilUrl: String ->
                zoomImageFromThumb(imageViewProfile, fotoProfilUrl)
            }
        )
        binding.recyclerViewChat.adapter = userChatRoomAdapter
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
    }

    private fun onClickUserChatRoom(user: User) {
        chatViewModel.storeChatRoom(bearerToken, user.id!!)
    }

    private fun zoomImageFromThumb(imageViewProfile: ImageView, fotoProfilUrl: String) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        // It will be loaded to the SubsamplingScaleImageView using SubsamplingScaleImageViewTarget class //
        // And image will be loaded with Glide //
        Glide
            .with(this)
            .download(GlideUrl(fotoProfilUrl))
            .into(SubsamplingScaleImageViewTarget(binding.expandedImageViewProfile))


        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        imageViewProfile.getGlobalVisibleRect(startBoundsInt)
        binding.root
            .getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        imageViewProfile.alpha = 0f
        binding.expandedImageViewProfile.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        binding.expandedImageViewProfile.pivotX = 0f
        binding.expandedImageViewProfile.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    binding.expandedImageViewProfile,
                    View.X,
                    startBounds.left,
                    finalBounds.left)
            ).apply {
                with(ObjectAnimator.ofFloat(binding.expandedImageViewProfile, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(binding.expandedImageViewProfile, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(binding.expandedImageViewProfile, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()

            // Setting other views to invisible
            binding.containerCreateChatRoom.alpha = 0f
        }

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        binding.expandedImageViewProfile.setOnClickListener {
            currentAnimator?.cancel()

            // Setting other views to visible
            binding.containerCreateChatRoom.alpha = 1f

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(binding.expandedImageViewProfile, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(binding.expandedImageViewProfile, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(binding.expandedImageViewProfile, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(binding.expandedImageViewProfile, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        imageViewProfile.alpha = 1f
                        binding.expandedImageViewProfile.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        imageViewProfile.alpha = 1f
                        binding.expandedImageViewProfile.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }

    private fun getUserChatRoom() {
        userViewModel.findUsersByRole(bearerToken)
        displayUserChatRoom()
    }

    private fun displayUserChatRoom() {
        userViewModel.userApiResponse.observe(this, Observer {
            if(it.code == 200 && it.listUser != null) {
                userChatRoomAdapter.setList(it.listUser)
                userChatRoomAdapter.notifyDataSetChanged()
            } else {
                /*Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()*/
                Helper.createSnackbar(binding.root, it.message!!).show()
            }
        })
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            bearerToken = "Bearer $bearerToken"
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat Baru"

        // observe loading status from the view model
        observeLoadingStatus()

        chatViewModel.chatApiResponse.observe(this, Observer {
            Log.d("ChatRoom", it.toString())
            /*Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()*/

            if(it.code == 200 && it.chatRoom != null) {
                startActivity(
                    Intent(this, DetailChatRoomActivity::class.java)
                        .putExtra(DetailChatRoomActivity.EXTRA_CHAT_ROOM_ID, it.chatRoom.id)
                )
                finish()
            }
            if(it.code == 400 && it.chatRoom != null) {
                startActivity(
                    Intent(this, DetailChatRoomActivity::class.java)
                        .putExtra(DetailChatRoomActivity.EXTRA_CHAT_ROOM_ID, it.chatRoom.id)
                )
                finish()
            }
        })

        binding.swipeRefreshData.setOnRefreshListener {
            getUserChatRoom()
            binding.swipeRefreshData.isRefreshing = false
        }
    }

    private fun observeLoadingStatus() {
        chatViewModel.loadingProgressBar.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}