package com.bouzo.specials.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import com.bouzo.specials.R
import kotlinx.android.synthetic.main.deal_item.view.*

class ResizableDealCard(context: Context, attrs: AttributeSet? = null) :
    CardView(context, attrs) {

    var variant: Int = SIZE_BIG

    init {
        View.inflate(context, R.layout.deal_item, this)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ResizableDealCard,
            0, 0
        ).apply {
            try {
                getString(R.styleable.ResizableDealCard_dealName)?.let { deal_name.text = it }
                getString(R.styleable.ResizableDealCard_originalPrice)?.let {
                    deal_original.text = it
                }
                getString(R.styleable.ResizableDealCard_reducedPrice)?.let { deal_price.text = it }
                getResourceId(R.styleable.ResizableDealCard_dealImage, 0).takeIf { it != 0 }
                    ?.let { resourceId ->
                        val drawable = AppCompatResources.getDrawable(context, resourceId)
                        deal_image.setImageDrawable(drawable)
                    }
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val nameSize = when (variant) {
            SIZE_TINY -> R.dimen.deal_text_name_tiny
            SIZE_SMALL -> R.dimen.deal_text_name_small
            SIZE_MEDIUM -> R.dimen.deal_text_name_medium
            else -> R.dimen.deal_text_name_large
        }
        val priceSize = when (variant) {
            SIZE_TINY -> R.dimen.deal_text_price_tiny
            SIZE_SMALL -> R.dimen.deal_text_price_small
            SIZE_MEDIUM -> R.dimen.deal_text_price_medium
            else -> R.dimen.deal_text_price_large
        }

        deal_price.textSize = resources.getDimension(priceSize)
        deal_original.textSize = resources.getDimension(priceSize)
        deal_name.textSize = resources.getDimension(nameSize)

        val tall = MeasureSpec.getSize(heightMeasureSpec) > MeasureSpec.getSize(widthMeasureSpec)
        deal_name.maxLines = when (variant) {
            SIZE_SMALL, SIZE_TINY -> if (tall) 2 else 1
            else -> 2
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    companion object {
        const val SIZE_TINY = 0
        const val SIZE_SMALL = 1
        const val SIZE_MEDIUM = 2
        const val SIZE_BIG = 3
    }
}