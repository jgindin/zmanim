package com.gindin.zehutyosef;

import com.gindin.zehutyosef.berachot.BirkatAmmazon;
import com.gindin.zehutyosef.berachot.MeenShalosh;
import com.gindin.zehutyosef.tefilla.Arvit;
import com.gindin.zehutyosef.tefilla.Mincha;
import com.gindin.zehutyosef.tefilla.Shachrit;

public class SidurActivity
  extends ZehutYosefActivity {


  protected void addSections() {
    sections.add( new Shachrit() );
    sections.add( new Mincha() );
    sections.add( new Arvit() );
    sections.add( new BirkatAmmazon() );
    sections.add( new MeenShalosh() );
  }

}
