#!/bin/bash
set -e

# Create SVG for the high-res Aueraa.Digital app icon
cat << 'SVGEOF' > /tmp/aueraa_icon.svg
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="512" height="512">
  <defs>
    <linearGradient id="bgGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#080C14"/>
      <stop offset="100%" stop-color="#020408"/>
    </linearGradient>
    <linearGradient id="boxGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#0066FF"/>
      <stop offset="100%" stop-color="#0088FF"/>
    </linearGradient>
    <filter id="glow" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="4" stdDeviation="12" flood-color="#007BFF" flood-opacity="0.4"/>
    </filter>
  </defs>

  <!-- Background -->
  <rect width="512" height="512" rx="100" fill="url(#bgGrad)"/>
  
  <!-- Outer subtle tech border -->
  <rect x="24" y="24" width="464" height="464" rx="80" fill="none" stroke="#007BFF" stroke-width="4" stroke-opacity="0.3"/>

  <!-- Electric Blue Center Emblem Box -->
  <g filter="url(#glow)">
    <rect x="96" y="96" width="320" height="320" rx="36" fill="url(#boxGrad)"/>
  </g>

  <!-- Aueraa Crystal Origami Facets in Pure Crisp White (matching user logo) -->
  <g fill="none" stroke="#FFFFFF" stroke-width="8" stroke-linecap="round" stroke-linejoin="round">
    <!-- Outer contour -->
    <polygon points="256,132 384,248 356,364 256,324 156,364 128,248" fill="rgba(255,255,255,0.06)"/>

    <!-- Top Apex to central spine -->
    <line x1="256" y1="132" x2="256" y2="324"/>

    <!-- Central horizontal connector -->
    <line x1="128" y1="248" x2="384" y2="248"/>

    <!-- Diagonals from wings to center diamond -->
    <line x1="128" y1="248" x2="256" y2="290"/>
    <line x1="384" y1="248" x2="256" y2="290"/>
    <line x1="256" y1="132" x2="256" y2="290"/>

    <!-- Lower wings and facets -->
    <line x1="156" y1="364" x2="256" y2="290"/>
    <line x1="356" y1="364" x2="256" y2="290"/>
    <line x1="156" y1="364" x2="256" y2="248"/>
    <line x1="356" y1="364" x2="256" y2="248"/>

    <!-- Lower center notch to apex -->
    <line x1="256" y1="324" x2="256" y2="290"/>
  </g>

  <!-- Small digital accent dot -->
  <circle cx="256" cy="132" r="6" fill="#FFFFFF"/>
</svg>
SVGEOF

# Also create the wide logo banner for splash screen and headers
cat << 'SVGEOF' > /tmp/aueraa_banner.svg
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 240" width="800" height="240">
  <defs>
    <linearGradient id="bgDark" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#080C14"/>
      <stop offset="100%" stop-color="#030509"/>
    </linearGradient>
    <linearGradient id="blueBox" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#0066FF"/>
      <stop offset="100%" stop-color="#0088FF"/>
    </linearGradient>
  </defs>

  <rect width="800" height="240" rx="16" fill="url(#bgDark)"/>
  
  <!-- Logo frame matching user image -->
  <rect x="50" y="45" width="700" height="150" fill="#000000" stroke="#0080FF" stroke-width="5"/>

  <!-- Text "Aueraa.Digital" -->
  <text x="80" y="145" font-family="system-ui, -apple-system, sans-serif" font-size="70" font-weight="800" fill="#FFFFFF">Aueraa<tspan fill="#0080FF">.Digital</tspan></text>

  <!-- Right Blue Box with Emblem -->
  <rect x="560" y="47" width="188" height="146" fill="url(#blueBox)"/>

  <!-- Geometric Crystal Mark -->
  <g transform="translate(654, 120) scale(0.38)" fill="none" stroke="#FFFFFF" stroke-width="7" stroke-linecap="round" stroke-linejoin="round">
    <polygon points="0,-110 120,-10 90,95 0,60 -90,95 -120,-10" fill="rgba(255,255,255,0.08)"/>
    <line x1="0" y1="-110" x2="0" y2="60"/>
    <line x1="-120" y1="-10" x2="120" y2="-10"/>
    <line x1="-120" y1="-10" x2="0" y2="30"/>
    <line x1="120" y1="-10" x2="0" y2="30"/>
    <line x1="-90" y1="95" x2="0" y2="30"/>
    <line x1="90" y1="95" x2="0" y2="30"/>
    <line x1="-90" y1="95" x2="0" y2="-10"/>
    <line x1="90" y1="95" x2="0" y2="-10"/>
  </g>
</svg>
SVGEOF

# Render to app/src/main/res/drawable/
mkdir -p app/src/main/res/drawable
convert -background none /tmp/aueraa_icon.svg app/src/main/res/drawable/ic_aueraa_logo.png
convert -background none /tmp/aueraa_banner.svg app/src/main/res/drawable/aueraa_banner.png

# Generate mipmap icons for all resolutions
BASE="app/src/main/res"
for spec in mdpi:48 hdpi:72 xhdpi:96 xxhdpi:144 xxxhdpi:192; do
  dir="$BASE/mipmap-${spec%:*}"
  size="${spec#*:}"
  mkdir -p "$dir"
  rm -f "$dir/ic_launcher.webp" "$dir/ic_launcher_round.webp"
  convert /tmp/aueraa_icon.svg -resize "${size}x${size}!" "PNG32:$dir/ic_launcher.png"
  radius=$((size / 2))
  convert /tmp/aueraa_icon.svg -resize "${size}x${size}!" \
    \( -size "${size}x${size}" xc:none -fill white \
       -draw "circle $radius,$radius $radius,0" \) \
    -alpha set -compose DstIn -composite "PNG32:$dir/ic_launcher_round.png"
done

echo "Icon generation complete!"
