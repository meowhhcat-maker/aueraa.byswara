#!/bin/bash
set -e

# Draw the Aueraa.Digital Icon using ImageMagick canvas primitives
render_icon() {
  local SIZE=$1
  local OUT=$2
  local ROUND=$3

  # Compute coordinates scaled to SIZE
  # Box bounds: 20% to 80%
  local B1=$((SIZE * 18 / 100))
  local B2=$((SIZE * 82 / 100))
  local CR=$((SIZE * 12 / 100))

  # Facet coordinates
  local CX=$((SIZE / 2))
  local APEX_Y=$((SIZE * 26 / 100))
  local MID_Y=$((SIZE * 50 / 100))
  local BASE_Y=$((SIZE * 74 / 100))
  local NOTCH_Y=$((SIZE * 66 / 100))
  local INNER_Y=$((SIZE * 58 / 100))

  local WING_L=$((SIZE * 26 / 100))
  local WING_R=$((SIZE * 74 / 100))
  local BOT_L=$((SIZE * 32 / 100))
  local BOT_R=$((SIZE * 68 / 100))

  local SW=$((SIZE > 100 ? 3 : (SIZE > 50 ? 2 : 1)))

  # Base icon
  convert -size ${SIZE}x${SIZE} xc:"#080C14" \
    -fill "#007BFF" -draw "roundrectangle $B1,$B1 $B2,$B2 $CR,$CR" \
    -stroke "rgba(255,255,255,0.9)" -strokewidth $SW -fill "rgba(255,255,255,0.15)" \
    -draw "polygon $CX,$APEX_Y $WING_R,$MID_Y $BOT_R,$BASE_Y $CX,$NOTCH_Y $BOT_L,$BASE_Y $WING_L,$MID_Y" \
    -fill none \
    -draw "line $CX,$APEX_Y $CX,$NOTCH_Y" \
    -draw "line $WING_L,$MID_Y $WING_R,$MID_Y" \
    -draw "line $WING_L,$MID_Y $CX,$INNER_Y" \
    -draw "line $WING_R,$MID_Y $CX,$INNER_Y" \
    -draw "line $BOT_L,$BASE_Y $CX,$INNER_Y" \
    -draw "line $BOT_R,$BASE_Y $CX,$INNER_Y" \
    -draw "line $BOT_L,$BASE_Y $WING_L,$MID_Y" \
    -draw "line $BOT_R,$BASE_Y $WING_R,$MID_Y" \
    "$OUT"

  if [ "$ROUND" = "true" ]; then
    local RADIUS=$((SIZE / 2))
    convert "$OUT" \
      \( -size ${SIZE}x${SIZE} xc:none -fill white -draw "circle $RADIUS,$RADIUS $RADIUS,0" \) \
      -alpha set -compose DstIn -composite "PNG32:$OUT"
  fi
}

BASE="app/src/main/res"
for spec in mdpi:48 hdpi:72 xhdpi:96 xxhdpi:144 xxxhdpi:192; do
  dir="$BASE/mipmap-${spec%:*}"
  size="${spec#*:}"
  mkdir -p "$dir"
  rm -f "$dir/ic_launcher.webp" "$dir/ic_launcher_round.webp"
  render_icon "$size" "$dir/ic_launcher.png" "false"
  render_icon "$size" "$dir/ic_launcher_round.png" "true"
done

# Also generate a large 512x512 logo asset
render_icon 512 "app/src/main/res/drawable/ic_aueraa_logo.png" "false"

echo "All PNG icons successfully created!"
