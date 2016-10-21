package com.bnsantos.checkable;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnsantos.checkable.models.Animal;
import com.bnsantos.checkable.models.Cat;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

public class AnimalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int HOLDER_ITEM = 1;
  private static final int HOLDER_SECTION = 2;
  public static final int SECTION_SPECIE = 10;
  public static final int SECTION_BREED = 11;
  private final List<Animal> mAnimals;
  private List<Item> mItems;
  private ResizeOptions mResizeOptions;
  private int mCurrentSectionMode;

  public AnimalsAdapter(List<Animal> animals) {
    this.mAnimals = animals;
    mCurrentSectionMode = -1;
    setUpSection(SECTION_SPECIE);
    mResizeOptions = new ResizeOptions(192, 192); //TODO work on this
  }

  public void setUpSection(int sectionSpecie) {
    if(sectionSpecie != mCurrentSectionMode){
      mCurrentSectionMode = sectionSpecie;

      mItems = new ArrayList<>();

      Section current = null;
      if(sectionSpecie == SECTION_SPECIE){
        //TODO improve this later
        if (current == null) {
          current = new Section("Cat");
          mItems.add(new Item(null, current));
        }
        for (Animal animal : mAnimals) {
          if(animal instanceof Cat){
            current.count++;
          }else{
            if (current.name.equals("Cat")) {
              current = new Section("Dog");
              mItems.add(new Item(null, current));
            }
            current.count++;
          }
          mItems.add(new Item(animal, null));
        }
      }else if(sectionSpecie == SECTION_BREED){

      }
     }
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if(viewType == HOLDER_ITEM) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image, parent, false);
      return new ImageHolder(view);
    }else{
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_section, parent, false);
      return new SectionHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if(holder instanceof ImageHolder) {
      ((ImageHolder) holder).onBind(mItems.get(position).animal, mResizeOptions);
    }else if(holder instanceof SectionHolder){
      Section section = mItems.get(position).section;
      ((SectionHolder) holder).onBind(section.name, Integer.toString(section.count));
    }
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  @Override
  public int getItemViewType(int position) {
    return mItems.get(position).animal == null ? HOLDER_SECTION : HOLDER_ITEM;
  }

  private class ImageHolder extends RecyclerView.ViewHolder{
    final SimpleDraweeView mDrawee;

    ImageHolder(View itemView) {
      super(itemView);
      mDrawee = (SimpleDraweeView) itemView.findViewById(R.id.image);
    }

    void onBind(Animal animal, ResizeOptions options){
      ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(animal.getUrl())).setRotationOptions(RotationOptions.autoRotate()).setResizeOptions(options).build();
      DraweeController controller = Fresco.newDraweeControllerBuilder()
          .setImageRequest(imageRequest)
          .setOldController(mDrawee.getController())
          .build();
      mDrawee.setController(controller);
    }
  }

  private class SectionHolder extends RecyclerView.ViewHolder{
    private final TextView mText;
    private final TextView mCount;

    SectionHolder(View itemView) {
      super(itemView);
      mText = (TextView) itemView.findViewById(R.id.text);
      mCount = (TextView) itemView.findViewById(R.id.count);
    }

    void onBind(String section, String count){
      mText.setText(section);
      mCount.setText(count);
    }
  }

  private class Item {
    private final Animal animal;
    private final Section section;

    Item(Animal animal, Section section) {
      this.animal = animal;
      this.section = section;
    }
  }

  private class Section{
    final String name;
    int count;

    Section(String name) {
      this.name = name;
    }
  }
}
