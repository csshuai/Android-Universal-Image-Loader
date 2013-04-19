/*
 * Copyright (C) 2010 Neil Davies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This code is base on the Android Gallery widget and was Created
 * by Neil Davies neild001 'at' gmail dot com to be a Coverflow widget
 *
 * @author Neil Davies
 */
package android.support.mdroid.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

@SuppressWarnings("deprecation")
public class CoverFlow extends Gallery {

    /**
     * Graphics Camera used for transforming the matrix of ImageViews.
     */
    private final Camera mCamera = new Camera();

    /**
     * The maximum angle the Child ImageView will be rotated by.
     */
    private int mMaxRotationAngle = 45;

    /**
     * The maximum zoom on the centre Child.
     */
    private int mMaxZoom = -120;
    
    private float mZAxis = 100.0f;

    /**
     * The Centre of the Coverflow.
     */
    private int mCoveflowCenter;

    public CoverFlow(final Context context) {
        super(context);
        this.setStaticTransformationsEnabled(true);
    }

    public CoverFlow(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.R.attr.galleryStyle);
    }

    public CoverFlow(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);
    }

    /**
     * Get the max rotational angle of the image.
     * 
     * @return the mMaxRotationAngle
     */
    public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }

    @Override
    public void setAdapter(final SpinnerAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * Set the max rotational angle of each image.
     * 
     * @param maxRotationAngle
     *            the mMaxRotationAngle to set
     */
    public void setMaxRotationAngle(final int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    /**
     * Get the Max zoom of the centre image.
     * 
     * @return the mMaxZoom
     */
    public int getMaxZoom() {
        return mMaxZoom;
    }

    /**
     * Set the max zoom of the centre image.
     * 
     * @param maxZoom
     *            the mMaxZoom to set
     */
    public void setMaxZoom(final int maxZoom) {
        mMaxZoom = maxZoom;
    }

    public float getmZAxis() {
        return mZAxis;
    }

    public void setmZAxis(float mZAxis) {
        this.mZAxis = mZAxis;
    }
    
    /**
     * Get the Centre of the Coverflow.
     * 
     * @return The centre of this Coverflow.
     */
    private int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    /**
     * Get the Centre of the View.
     * 
     * @return The centre of the given view.
     */
    private static int getCenterOfView(final View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see #setStaticTransformationsEnabled(boolean)
     */
    @Override
    protected boolean getChildStaticTransformation(final View child, final Transformation t) {

        final int childCenter = getCenterOfView(child);
        final int childWidth = child.getWidth();
        int rotationAngle = 0;

        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);

        if (childCenter == mCoveflowCenter) {
            transformImageBitmap((ImageView) child, t, 0);
        } else {
            rotationAngle = (int) ((float) (mCoveflowCenter - childCenter) / childWidth * mMaxRotationAngle);
            if (Math.abs(rotationAngle) > mMaxRotationAngle) {
                rotationAngle = rotationAngle < 0 ? -mMaxRotationAngle : mMaxRotationAngle;
            }
            transformImageBitmap((ImageView) child, t, rotationAngle);
        }

        return true;
    }

    /**
     * This is called during layout when the size of this view has changed. If you were
     * just added to the view hierarchy, you're called with the old values of 0.
     * 
     * @param w
     *            Current width of this view.
     * @param h
     *            Current height of this view.
     * @param oldw
     *            Old width of this view.
     * @param oldh
     *            Old height of this view.
     */
    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * Transform the Image Bitmap by the Angle passed.
     * 
     * @param imageView
     *            ImageView the ImageView whose bitmap we want to rotate
     * @param t
     *            transformation
     * @param rotationAngle
     *            the Angle by which to rotate the Bitmap
     */
    private void transformImageBitmap(final ImageView child, final Transformation t, final int rotationAngle) {
        mCamera.save();
        final Matrix imageMatrix = t.getMatrix();

        final int height = child.getMeasuredHeight();
        final int width = child.getMeasuredWidth();
        final int rotation = Math.abs(rotationAngle);

        mCamera.translate(0.0f, 0.0f, mZAxis);

        // As the angle of the view gets less, zoom in
        if (rotation < mMaxRotationAngle) {
            final float zoomAmount = (float) (mMaxZoom + rotation * 4);
            mCamera.translate(0.0f, 0.0f, zoomAmount);
        }

        mCamera.rotateY(rotationAngle);
        mCamera.getMatrix(imageMatrix);

        imageMatrix.preTranslate(-(width / 2.0f), -(height / 2.0f));
        imageMatrix.postTranslate((width / 2.0f), (height / 2.0f));
        mCamera.restore();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int i1 = indexOfChild(getSelectedView());
        if (i1 < 0)
            return i;
        int i2 = childCount - 1;
        if (i >= i1) {
            int i3 = i2 - (i - i1);
            return i3;
        }
        return super.getChildDrawingOrder(childCount, i);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            super.onLayout(changed, l, t, r, b);
        }
    }
}