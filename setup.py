import os

base_dir = r"c:\MCA\3RD SEM\Lab\MAD\Project\MediRemind"

dirs = [
    r"app\src\main\java\com\example\mediremind\activities",
    r"app\src\main\java\com\example\mediremind\adapters",
    r"app\src\main\java\com\example\mediremind\database",
    r"app\src\main\java\com\example\mediremind\models",
    r"app\src\main\java\com\example\mediremind\receivers",
    r"app\src\main\java\com\example\mediremind\utils",
    r"app\src\main\res\layout",
    r"app\src\main\res\values",
    r"app\src\main\res\drawable",
    r"app\src\main\res\mipmap-mdpi",
]

for d in dirs:
    os.makedirs(os.path.join(base_dir, d), exist_ok=True)

# create basic values xmls
colors_xml = """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary">#1565C0</color>
    <color name="primary_dark">#0D47A1</color>
    <color name="primary_light">#42A5F5</color>
    <color name="medical_blue">#2196F3</color>
    <color name="background">#F5F9FF</color>
    <color name="white">#FFFFFF</color>
    <color name="soft_blue">#E3F2FD</color>
    <color name="very_light_blue">#F1F8FF</color>
    <color name="primary_text">#16324F</color>
    <color name="secondary_text">#607D8B</color>
    <color name="success">#2E7D32</color>
    <color name="success_background">#E8F5E9</color>
    <color name="warning">#EF6C00</color>
    <color name="warning_background">#FFF3E0</color>
    <color name="error">#D32F2F</color>
    <color name="error_background">#FFEBEE</color>
    <color name="divider">#E3EDF7</color>
</resources>
"""

with open(os.path.join(base_dir, r"app\src\main\res\values\colors.xml"), "w") as f:
    f.write(colors_xml)
    
strings_xml = """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">MediRemind</string>
    <string name="never_miss">Never Miss Your Medicine.</string>
</resources>
"""

with open(os.path.join(base_dir, r"app\src\main\res\values\strings.xml"), "w") as f:
    f.write(strings_xml)

themes_xml = """<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.MediRemind" parent="Theme.MaterialComponents.Light.NoActionBar">
        <item name="colorPrimary">@color/primary</item>
        <item name="colorPrimaryVariant">@color/primary_dark</item>
        <item name="colorOnPrimary">@color/white</item>
        <item name="colorSecondary">@color/medical_blue</item>
        <item name="colorSecondaryVariant">@color/primary_light</item>
        <item name="colorOnSecondary">@color/white</item>
        <item name="android:statusBarColor">@color/primary_dark</item>
        <item name="android:windowBackground">@color/background</item>
        <item name="android:textColorPrimary">@color/primary_text</item>
        <item name="android:textColorSecondary">@color/secondary_text</item>
    </style>

    <style name="Theme.MediRemind.Splash" parent="Theme.MediRemind">
        <item name="android:windowBackground">@color/white</item>
        <item name="android:statusBarColor">@color/white</item>
        <item name="android:windowLightStatusBar">true</item>
    </style>
</resources>
"""

with open(os.path.join(base_dir, r"app\src\main\res\values\themes.xml"), "w") as f:
    f.write(themes_xml)

print("Setup complete")
