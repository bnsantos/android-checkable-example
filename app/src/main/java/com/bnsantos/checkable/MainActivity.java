package com.bnsantos.checkable;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.bnsantos.checkable.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private ActivityMainBinding mBinding;
  private AnimalsAdapter mAdapter;
  private ActionMode.Callback mActionModeCallBack;
  private ActionMode mActionMode;


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
    mBinding.recyclerView.setLayoutManager(create(i));
    mBinding.recyclerView.getAdapter().notifyDataSetChanged();
  }

  private void initAdapter(){
    mBinding.recyclerView.setLayoutManager(create(2));
    mAdapter = new AnimalsAdapter(this, ((App) getApplication()).loadData(), getResources().getDimensionPixelOffset(R.dimen.padding));
    mBinding.recyclerView.setAdapter(mAdapter);
  }

  private void groupBySpecie() {
    mAdapter.setUpSection(AnimalsAdapter.SECTION_SPECIE);
  }

  private void groupByBreed() {
    mAdapter.setUpSection(AnimalsAdapter.SECTION_BREED);
  }

  private RecyclerView.LayoutManager create(final int span){
    GridLayoutManager layoutManager = new GridLayoutManager(this, span);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        return mAdapter.getItemViewType(position) == AnimalsAdapter.HOLDER_ITEM ? 1 : span;
      }
    });
    return layoutManager;
  }

  public void startActionMode(){
    mActionModeCallBack = new ActionMode.Callback() {
      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_mode, menu);
        return true;
      }

      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
      }

      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
      }

      @Override
      public void onDestroyActionMode(ActionMode mode) {
        finishActionMode();
      }
    };
    mActionMode = startSupportActionMode(mActionModeCallBack);
  }

  public void finishActionMode() {
    if(mActionMode!=null){
      mActionMode.finish();
    }
    mAdapter.finishActionMode();
  }

  public void setActionModeTitle(String title){
    if(mActionMode != null){
      mActionMode.setTitle(title);
    }
  }
}
