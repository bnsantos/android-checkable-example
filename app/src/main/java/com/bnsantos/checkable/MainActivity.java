package com.bnsantos.checkable;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.bnsantos.checkable.databinding.ActivityMainBinding;
import com.bnsantos.checkable.models.Animal;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private ActivityMainBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    setSupportActionBar(mBinding.toolbar);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, mBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mBinding.drawerLayout.setDrawerListener(toggle);
    toggle.syncState();

    mBinding.navView.setNavigationItemSelectedListener(this);

    initAdapter();
  }

  @Override
  public void onBackPressed() {
    if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      mBinding.drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()){
      case R.id.specie:
        groupBySpecie();
        break;
      case R.id.breed:
        groupByBreed();
        break;
      case R.id.two:
        setItemsPerColumn(2);
        break;
      case R.id.three:
        setItemsPerColumn(3);
        break;
    }

    mBinding.drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  private void setItemsPerColumn(int i) {
    mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, i));
    mBinding.recyclerView.getAdapter().notifyDataSetChanged();
  }

  private void initAdapter(){
    mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    mBinding.recyclerView.setAdapter(new AnimalsAdapter(((App) getApplication()).loadData()));
  }

  private void groupBySpecie() {
    Toast.makeText(this, "TODO - Group by specie", Toast.LENGTH_SHORT).show();
  }

  private void groupByBreed() {
    Toast.makeText(this, "TODO - Group by breed", Toast.LENGTH_SHORT).show();
  }
}
