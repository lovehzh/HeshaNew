package com.hesha.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PageIndicator extends View
{
  private int defaultSize = 4;
  private int defaultIndex = 0;
  private int indicatorLength;
  private float startX;
  private final int color = -39271;
  private Paint paint;

  public PageIndicator(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setup();
  }

  public PageIndicator(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    setup();
  }

  private void setup()
  {
    this.paint = new Paint();
    this.paint.setColor(color);
  }

  public final void changePosition(float originalPosition, int step)
  {
    this.startX = (this.indicatorLength * (originalPosition + step));
    postInvalidate();
  }

  public final void setIndex(int index)//a
  {
    this.defaultIndex = index;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(this.startX, 0.0F, this.startX + this.indicatorLength, 0.0F, this.paint);
  }

  protected void onSizeChanged(int w, int h, int oldW, int oldH)
  {
    super.onSizeChanged(w, h, oldW, oldH);
    this.indicatorLength = (w / this.defaultSize);
    this.startX = (this.indicatorLength * this.defaultIndex);
    this.paint.setStrokeWidth(h + 1);
  }
}