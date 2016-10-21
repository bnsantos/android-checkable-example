package com.bnsantos.checkable;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnsantos.checkable.models.Animal;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

public class AnimalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int HOLDER_ITEM = 1;
  public static final int HOLDER_DIVIDER = 2;
  private final List<Animal> mAnimals;
  private ResizeOptions mResizeOptions;

  public AnimalsAdapter(List<Animal> animals) {
    this.mAnimals = animals;
    mResizeOptions = new ResizeOptions(192, 192); //TODO work on this
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image, parent, false);
    return new ImageHolder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ((ImageHolder)holder).onBind(mAnimals.get(position), mResizeOptions);
  }

  @Override
  public int getItemCount() {
    return mAnimals.size();
  }

  private class ImageHolder extends RecyclerView.ViewHolder{
    public final SimpleDraweeView mDrawee;

    public ImageHolder(View itemView) {
      super(itemView);
      mDrawee = (SimpleDraweeView) itemView.findViewById(R.id.image);
    }

    public void onBind(Animal animal, ResizeOptions options){
      ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(animal.getUrl())).setRotationOptions(RotationOptions.autoRotate()).setResizeOptions(options).build();
      DraweeController controller = Fresco.newDraweeControllerBuilder()
          .setImageRequest(imageRequest)
          .setOldController(mDrawee.getController())
          .build();
      mDrawee.setController(controller);
    }
  }
}
