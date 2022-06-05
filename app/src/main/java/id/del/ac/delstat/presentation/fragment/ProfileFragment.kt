package id.del.ac.delstat.presentation.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentProfileBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.activity.LoginActivity
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.util.Helper


class ProfileFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userPreferences: UserPreferences
    private var fotoProfilUrl: String = ""
    var bearerToken: String? = null

    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myReturnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        myReturnTransition.duration = 200

        val myReenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        myReenterTransition.duration = 500

        val myExitTransition = MaterialFadeThrough()
        myExitTransition.duration = 500

        enterTransition = myReenterTransition
        exitTransition = myExitTransition
        reenterTransition = myReenterTransition
        returnTransition = myReturnTransition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view)
        userViewModel = (activity as HomeActivity).userViewModel
        userPreferences = (activity as HomeActivity).userPreferences
        bearerToken = (activity as HomeActivity).bearerToken

        userPreferences.getUserToken.asLiveData().observe(viewLifecycleOwner) {
            // Log.d("MyTag", "ProfileFragment Token: $it")
            if (it.isNullOrEmpty()) {
                binding.containerNotLogin.visibility = View.VISIBLE
                binding.containerLogin.visibility = View.GONE
                bearerToken = ""
            } else {
                binding.containerLogin.visibility = View.VISIBLE
                binding.containerNotLogin.visibility = View.GONE
                bearerToken = it
            }
        }

        prepareUI()
    }

    private fun logout() {
        bearerToken = "Bearer $bearerToken"
        userViewModel.logout(bearerToken!!)
        bearerToken = ""
        (activity as HomeActivity).bearerToken = bearerToken
    }

    private fun prepareUI() {
        userPreferences.getUserNama.asLiveData().observe(viewLifecycleOwner) {
            binding.textViewNamaProfile.text = it
        }
        userPreferences.getUserEmail.asLiveData().observe(viewLifecycleOwner) {
            binding.textViewEmailProfile.text = it
        }
        userPreferences.getUserNoHp.asLiveData().observe(viewLifecycleOwner) {
            binding.textViewNoHpProfile.text = it
        }
        userPreferences.getUserJenjang.asLiveData().observe(viewLifecycleOwner) {
            binding.textViewJenjangProfile.text = it
        }
        userPreferences.getUserFotoProfil.asLiveData().observe(viewLifecycleOwner) {
            fotoProfilUrl = BuildConfig.BASE_URL + it
            Log.d("MyTag", "FotoProfilUrl: $fotoProfilUrl")
            if (!it.isNullOrEmpty()) {
                /*Glide.with(this)
                    .load(fotoProfilUrl)
                    .placeholder(R.drawable.ic_default_foto_profil)
                    .error(R.drawable.ic_default_foto_profil)
                    .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis().toString())))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageViewProfile)*/
                Helper.showImageGlide(requireContext(), fotoProfilUrl, binding.imageViewProfile)
            }
        }

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        binding.imageViewProfile.setOnClickListener {
            zoomImageFromThumb(binding.imageViewProfile, fotoProfilUrl)
        }

        binding.buttonLogin.setOnClickListener {
            // Log.d("MyTag", "Button Login Clicked")
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.buttonLogout.setOnClickListener {
            logout()
        }

        binding.buttonEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    private fun zoomImageFromThumb(imageViewProfile: ImageView, fotoProfilUrl: String) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        Helper.showImageGlide(requireContext(), fotoProfilUrl, binding.expandedImageViewProfile)

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
        binding.imageViewProfile.getGlobalVisibleRect(startBoundsInt)
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
        binding.imageViewProfile.alpha = 0f
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
        }

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        binding.expandedImageViewProfile.setOnClickListener {
            currentAnimator?.cancel()

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
                        binding.imageViewProfile.alpha = 1f
                        binding.expandedImageViewProfile.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        binding.imageViewProfile.alpha = 1f
                        binding.expandedImageViewProfile.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }

}