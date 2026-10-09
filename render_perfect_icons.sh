#!/bin/bash
set -e

# Render high-resolution master asset (1024x1024)
# Colors from user's image:
# Blue: #007BFF (RGB: 0, 123, 255)
# Dark: #080C14 or #000000
# White lines: #FFFFFF

MASTER_SIZE=1024
OUT_MASTER="/tmp/aueraa_master_icon.png"

# Blue box is centered:
# Box bounds: 120 to 904 (size 784x784) with rounded corners radius 90
B1=128
B2=896
CR=96

# Geometry coordinates on 1024x1024 canvas
# Center is 512, 512
APEX_X=512
APEX_Y=256

WING_L_X=240
WING_L_Y=490

WING_R_X=784
WING_R_Y=490

BOT_L_X=330
BOT_L_Y=768

BOT_R_X=694
BOT_R_Y=768

NOTCH_X=512
NOTCH_Y=670

MID_X=512
MID_Y=550

SW=18 # Crisp thick white stroke

convert -size ${MASTER_SIZE}x${MASTER_SIZE} xc:"#000000" \
  -fill "#007BFF" -draw "roundrectangle $B1,$B1 $B2,$B2 $CR,$CR" \
  -stroke "#FFFFFF" -strokewidth $SW -fill "rgba(255,255,255,0.06)" \
  -draw "polygon $APEX_X,$APEX_Y $WING_R_X,$WING_R_Y $BOT_R_X,$BOT_R_Y $NOTCH_X,$NOTCH_Y $BOT_L_X,$BOT_L_Y $WING_L_X,$WING_L_Y" \
  -fill none \
  -draw "line $APEX_X,$APEX_Y $NOTCH_X,$NOTCH_Y" \
  -draw "line $WING_L_X,$WING_L_Y $WING_R_X,$WING_R_Y" \
  -draw "line $WING_L_X,$WING_L_Y $MID_X,$MID_Y" \
  -draw "line $WING_R_X,$WING_R_Y $MID_X,$MID_Y" \
  -draw "line $BOT_L_X,$BOT_L_Y $MID_X,$MID_Y" \
  -draw "line $BOT_R_X,$BOT_R_Y $MID_X,$MID_Y" \
  -draw "line $BOT_L_X,$BOT_L_Y $WING_L_X,$WING_L_Y" \
  -draw "line $BOT_R_X,$BOT_R_Y $WING_R_X,$WING_R_Y" \
  "$OUT_MASTER"

# Also render full banner image (matching the exact banner with black border, text and logo box)
BANNER_OUT="app/src/main/res/drawable/aueraa_logo_banner.png"
convert -size 1200x400 xc:"#000000" \
  -stroke "#007BFF" -strokewidth 6 -fill "#000000" \
  -draw "rectangle 60,60 1140,340" \
  -fill "#007BFF" -stroke none \
  -draw "roundrectangle 860,63 1137,337 10,10" \
  /tmp/banner_base.png

# Composite resized emblem onto banner right box
convert "$OUT_MASTER" -resize 260x260 /tmp/emblem_small.png
convert /tmp/banner_base.png /tmp/emblem_small.png -geometry +870+70 -composite "$BANNER_OUT"

# Also save master app icon drawable
cp "$OUT_MASTER" app/src/main/res/drawable/ic_aueraa_logo.png
convert "$OUT_MASTER" -resize 512x512 app/src/main/res/drawable/aueraa_app_icon.png

# Now populate all 5 mipmap densities for both square and round launcher icons
BASE="app/src/main/res"
for spec in mdpi:48 hdpi:72 xhdpi:96 xxhdpi:144 xxxhdpi:192; do
  dir="$BASE/mipmap-${spec%:*}"
  size="${spec#*:}"
  mkdir -p "$dir"
  rm -f "$dir/ic_launcher.webp" "$dir/ic_launcher_round.webp"
  
  # Square launcher icon
  convert "$OUT_MASTER" -filter Lanczos -resize "${size}x${size}!" "PNG32:$dir/ic_launcher.png"
  
  # Round launcher icon with circular alpha mask
  radius=$((size / 2))
  convert "$OUT_MASTER" -filter Lanczos -resize "${size}x${size}!" \
    \( -size "${size}x${size}" xc:none -fill white -draw "circle $radius,$radius $radius,0" \) \
    -alpha set -compose DstIn -composite "PNG32:$dir/ic_launcher_round.png"
done

echo "Successfully generated high-definition Aueraa.Digital icons across all resource densities!"
