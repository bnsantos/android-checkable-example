package com.bnsantos.checkable;

import android.app.Application;

import com.bnsantos.checkable.models.Animal;
import com.bnsantos.checkable.models.Cat;
import com.bnsantos.checkable.models.Dog;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class App extends Application {
  public static final String[] SPECIES = new String[]{
      "cats",
      "dogs"
  };

  public static final int[] CATS = new int[]{
      R.array.cats_siamese,
      R.array.cats_persian,
      R.array.cats_bengals,
      R.array.cats_bombay,
      R.array.cats_unknown
  };

  public static final String[] CATS_BREEDS = new String[]{
      "siamese",
      "persian",
      "bengals",
      "bombay",
      "unknown"
  };

  public static final int[] DOGS = new int[]{
      R.array.dogs_pug,
      R.array.dogs_bulldog,
      R.array.dogs_labrador,
      R.array.dogs_siberian_husky,
      R.array.dogs_unknown
  };

  public static final String[] DOGS_BREEDS = new String[]{
      "pug",
      "bulldog",
      "labrador",
      "siberian_husky",
      "unknown"
  };

  @Override
  public void onCreate() {
    super.onCreate();
    Fresco.initialize(this);
  }

  public List<Animal> loadData(){
    List<Animal> animals = new ArrayList<>();

    //Load cats
    animals.addAll(loadSpecie(Cat.class, CATS));
    animals.addAll(loadSpecie(Dog.class, DOGS));

    return animals;
  }

  private List<Animal> loadSpecie(Class clazz, int[] resources){
    List<Animal> specie = new ArrayList<>();
    int breed = 0;
    for (int resource : resources) {
      String[] urls = getResources().getStringArray(resource);
      for (String url : urls) {
        if(clazz.getName().equals(Cat.class.getName())){
          specie.add(new Cat(CATS_BREEDS[breed], url));
        }else{
          specie.add(new Dog(DOGS_BREEDS[breed], url));
        }
      }
      breed ++;
    }
    return specie;
  }
}
