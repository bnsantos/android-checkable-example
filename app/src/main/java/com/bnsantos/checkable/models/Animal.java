package com.bnsantos.checkable.models;


public abstract class Animal {
  private final String mBreed;
  private final String mUrl;


  public Animal(String breed, String url) {
    this.mBreed = breed;
    this.mUrl = url;
  }

  public String getBreed() {
    return mBreed;
  }

  public String getUrl() {
    return mUrl;
  }
}
