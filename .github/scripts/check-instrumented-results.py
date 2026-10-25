#!/usr/bin/env python3
# Copyright (c) 2026. Jay R. Gindin
"""Fails unless :Zmanim and each :ZehutYosef flavor wrote on-device test results that ran tests and had no failures.

A connectedXxxAndroidTest task finishes successfully, with no results at all, when the app APK fails to install, and
with an empty result when the instrumentation crashes before its first test (for example on a class R8 removed from
the app). Checking only that some results exist would let a module that never ran pass beside one that did, so every
module is checked on its own and named in the failure.

A test that skips itself with a JUnit Assume (for example one that needs a newer Android than the emulator runs) is
written by the Android Gradle plugin as a <failure> that holds an AssumptionViolatedException. Those are skips: they
are not counted as failures, and a module must still have at least one test that ran.
"""
import glob
import sys
import xml.etree.ElementTree as ET

# Display name -> directory holding that module's (or flavor's) junit XML. Gradle writes a flavor's results under
#  connected/<variant>/flavors/<flavor>/ and an unflavored module's directly under connected/<variant>/.
ASSUMPTION_SKIP = "AssumptionViolatedException"

MODULES = {
    "Zmanim": "Zmanim/build/outputs/androidTest-results/connected/*",
    "ZehutYosef barMitzvah": "ZehutYosef/build/outputs/androidTest-results/connected/*/flavors/barMitzvah",
    "ZehutYosef sidur": "ZehutYosef/build/outputs/androidTest-results/connected/*/flavors/sidur",
}


def main() -> int:
    failed = False
    for module, pattern in MODULES.items():
        files = sorted(glob.glob(f"{pattern}/TEST-*.xml"))
        if not files:
            print(f"::error::{module} produced no instrumented test results; its app or test APK most likely failed to install")
            failed = True
            continue
        ran = bad = 0
        for path in files:
            for case in ET.parse(path).getroot().iter("testcase"):
                problem = case.find("failure")
                if problem is None:
                    problem = case.find("error")
                if problem is None:
                    ran += 1
                elif ASSUMPTION_SKIP not in ((problem.text or "") + (problem.get("message") or "")):
                    ran += 1
                    bad += 1
        if ran == 0:
            print(f"::error::{module} ran no instrumented tests (all skipped or none started); the instrumentation most likely crashed before the first one")
            failed = True
        elif bad > 0:
            print(f"::error::{module} had {bad} failing instrumented tests out of {ran}")
            failed = True
        else:
            print(f"{module}: {ran} instrumented tests ran, none failing")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
