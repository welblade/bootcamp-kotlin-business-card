package com.github.welblade.businesscard.util

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import com.google.android.material.card.MaterialCardView

class ListItemBusinessCardAnimator {
    companion object{
        fun rotateCardView(cardView: MaterialCardView): ObjectAnimator? {
            return ObjectAnimator.ofFloat(cardView, "rotationY", 89f).apply{
                duration = 250
                start()
                doOnEnd {
                    cardView.apply {
                        if (getChildAt(0).visibility == View.VISIBLE) {
                            getChildAt(0).visibility = View.GONE
                            getChildAt(1).visibility = View.VISIBLE
                        } else {
                            getChildAt(1).visibility = View.GONE
                            getChildAt(0).visibility = View.VISIBLE
                        }
                    }
                    cardView.rotationY = -90f
                    ObjectAnimator.ofFloat(cardView, "rotationY", 0f).apply{
                        duration = 250
                        start()
                    }

                }
            }

        }
    }
}

