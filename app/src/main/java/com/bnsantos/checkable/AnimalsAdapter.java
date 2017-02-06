package com.bnsantos.checkable;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Arrays;
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
  private final MainActivity mListener;
  private SparseBooleanArray mSelected = new SparseBooleanArray();
  private final int mPadding;

  public AnimalsAdapter(MainActivity listener, List<Animal> animals, int padding) {
    mListener = listener;
    this.mAnimals = animals;
    mCurrentSectionMode = -1;
    setUpSection(SECTION_SPECIE);
    mResizeOptions = new ResizeOptions(192, 192); //TODO work on this
    mPadding = padding;
  }

  public void setUpSection(int sectionSpecie) {
    if(sectionSpecie != mCurrentSectionMode){
      mCurrentSectionMode = sectionSpecie;

      mItems = new ArrayList<>();

      Section current = null;
      if(sectionSpecie == SECTION_SPECIE){
        int specieIdx = 0;
        current = new Section(App.SPECIES[specieIdx++]);
        mItems.add(new Item(null, current, 0));
        for (Animal animal : mAnimals) {
          if(animal instanceof Cat){
            current.count++;
          }else{
            if (!current.name.equals(App.SPECIES[1])) {
              current = new Section(App.SPECIES[specieIdx++]);
              mItems.add(new Item(null, current, 0));
            }
            current.count++;
          }
          mItems.add(new Item(animal, current, current.count));
        }
      }else if(sectionSpecie == SECTION_BREED){
        List<String> breeds = new ArrayList<>();
        breeds.addAll(Arrays.asList(App.CATS_BREEDS));
        breeds.addAll(Arrays.asList(App.DOGS_BREEDS));
        for (String breed : breeds) {
          current = new Section(breed);
          mItems.add(new Item(null, current, 0));
          for (Animal animal : mAnimals) {
            if(animal.getBreed().equals(breed)){
              current.count++;
              mItems.add(new Item(animal, current, current.count));
            }
          }
        }
      }
    }
    notifyDataSetChanged();
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
      ((ImageHolder) holder).onBind(mItems.get(position).animal, mResizeOptions, mSelected.get(position), isActionMode(), mPadding);
    }else if(holder instanceof SectionHolder){
      Section section = mItems.get(position).section;
      ((SectionHolder) holder).onBind(section.name, Integer.toString(section.count), mSelected.get(position), isActionMode());
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

  public void finishActionMode() {
    mSelected.clear();
    notifyDataSetChanged();
  }

  private void toggleItem(RecyclerView.ViewHolder holder){
    int pos = holder.getAdapterPosition();
    if(!isActionMode()){
      mListener.startActionMode();
      notifyDataSetChanged();
    }

    boolean selectGroup;
    if(mSelected.get(pos)){
      mSelected.delete(pos);
      selectGroup = false;
    }else{
      mSelected.append(pos, true);
      selectGroup = true;
    }

    notifyItemChanged(pos);
    Item item = mItems.get(pos);
    Section section = item.section;
    if(section != null){
      if(holder instanceof SectionHolder){
        for (int i = pos + 1; i<mItems.size() && i<=pos+section.count; i++ ){
          if(selectGroup) {
            mSelected.append(i, true);
          }else{
            mSelected.delete(i);
          }
          notifyItemChanged(i);
        }
      }else {
        if(selectGroup) {
          section.selected++;
        }else{
          section.selected--;
        }
        int sectionPos = pos-item.sectionOffset;
        if(section.count == section.selected){
          if(selectGroup) {
            mSelected.append(sectionPos, true);
            notifyItemChanged(sectionPos);
          }
        }else{
          if(!selectGroup) {
            mSelected.delete(sectionPos);
            notifyItemChanged(sectionPos);
          }
        }
      }
    }

    int size = mSelected.size();
    if(size == 0){
      mListener.finishActionMode();
    }else {
      mListener.setActionModeTitle(Integer.toString(size));
    }
  }

  boolean isActionMode(){
    return mSelected.size()!=0;
  }

  private class ImageHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
    final DraweeCheckable mView;

    ImageHolder(View itemView) {
      super(itemView);
      itemView.setOnLongClickListener(this);
      itemView.setOnClickListener(this);
      mView = (DraweeCheckable) itemView;
    }

    void onBind(Animal animal, ResizeOptions options, boolean checked, boolean actionMode, int padding){
      mView.setImage(Uri.parse(animal.getUrl()), options);
      mView.setSelectable(actionMode);
      mView.setChecked(checked);
    }

    @Override
    public boolean onLongClick(View v) {
      toggleItem(this);
      return true;
    }

    @Override
    public void onClick(View v) {
      if (isActionMode()) {
        toggleItem(this);
      }
    }
  }

  private class SectionHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
    private final CheckableLinearLayout mLayout;
    private final TextView mText;
    private final TextView mCount;
    private final ImageView mSelector;

    SectionHolder(View itemView) {
      super(itemView);
      mLayout = (CheckableLinearLayout) itemView;
      mText = (TextView) itemView.findViewById(R.id.text);
      mCount = (TextView) itemView.findViewById(R.id.count);
      mSelector = (ImageView) itemView.findViewById(R.id.selector);
      itemView.setOnLongClickListener(this);
      itemView.setOnClickListener(this);
    }

    void onBind(String section, String count, boolean checked, boolean actionMode){
      mText.setText(section);
      mCount.setText(count);
      if(!actionMode){
        mSelector.setVisibility(View.GONE);
      }else{
        mSelector.setVisibility(View.VISIBLE);
        mLayout.setChecked(checked);
      }
    }

    @Override
    public boolean onLongClick(View v) {
      toggleItem(this);
      return true;
    }

    @Override
    public void onClick(View v) {
      if (isActionMode()) {
        toggleItem(this);
      }
    }
  }

  private class Item {
    final Animal animal;
    final Section section;
    final int sectionOffset;

    Item(Animal animal, Section section, int sectionOffset) {
      this.animal = animal;
      this.section = section;
      this.sectionOffset = sectionOffset;
    }
  }

  private class Section{
    final String name;
    int count;
    int selected;

    Section(String name) {
      this.name = name;
    }
  }
}
