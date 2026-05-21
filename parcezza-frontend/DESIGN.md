---
version: "alpha"
name: "Parcezza Commerce"
description: "A premium, warm, and harmonious e-commerce design system focusing on reddish tones."
colors:
  primary: "#2C1011"
  secondary: "#7A3B3D"
  tertiary: "#E53935"
  neutral: "#FCF8F8"
  surface: "#FFFFFF"
  on-tertiary: "#FFFFFF"
typography:
  h1:
    fontFamily: "Playfair Display, serif"
    fontSize: "3rem"
    fontWeight: "700"
  body-md:
    fontFamily: "Inter, sans-serif"
    fontSize: "1rem"
    lineHeight: "1.5"
  label-caps:
    fontFamily: "Inter, sans-serif"
    fontSize: "0.875rem"
    fontWeight: "600"
    letterSpacing: "0.05em"
rounded:
  sm: "4px"
  md: "8px"
  lg: "16px"
spacing:
  sm: "8px"
  md: "16px"
  lg: "24px"
  xl: "32px"
components:
  button-primary:
    backgroundColor: "{colors.tertiary}"
    textColor: "{colors.on-tertiary}"
    rounded: "{rounded.md}"
    padding: "12px 24px"
  button-primary-hover:
    backgroundColor: "#C62828"
  product-card:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.lg}"
    padding: "{spacing.md}"
---

## Overview

**Parcezza Commerce** is a premium design system tailored for a modern e-commerce platform. It leverages a palette of harmonious reddish tones to evoke passion, energy, and elegance, balanced by a warm, inviting neutral background. The design marries the classic elegance of serif headings with the crisp legibility of modern sans-serif body text.

## Colors

The color palette is designed to guide the user's attention, highlight key actions (like "Add to Cart"), and maintain a luxurious shopping environment.

- **Primary (#2C1011):** A deep, dark burgundy used for primary text, headings, and high-contrast elements. It provides readability while remaining within the red spectrum.
- **Secondary (#7A3B3D):** A muted, sophisticated crimson for secondary text, borders, and inactive UI elements.
- **Tertiary (#E53935):** A vibrant, energetic red acting as the main call-to-action (CTA) color. This drives conversions and interactions.
- **Neutral (#FCF8F8):** A very light, warm off-white with a subtle pinkish undertone. It serves as the foundational background, softer and more cohesive than pure white.
- **Surface (#FFFFFF):** Pure white used for product cards and floating elements to ensure products pop against the neutral background.

## Typography

Typography pairs classic luxury with modern utility to ensure the e-commerce experience is both beautiful and highly usable.

- **h1:** `Playfair Display`. Used for elegant, impactful page titles and main promotions.
- **body-md:** `Inter`. A highly readable, neutral sans-serif for product descriptions, reviews, and general text.
- **label-caps:** `Inter` (uppercase, spaced). Used for small UI labels, buttons, and navigation, providing clear, structured wayfinding.

## Layout

The layout emphasizes generous spacing to let products breathe, creating a gallery-like browsing experience. 

- Use `{spacing.xl}` between major page sections.
- Use `{spacing.md}` within product grids to separate items comfortably.

## Elevation & Depth

Shadows and elevation should be kept subtle to maintain a modern, flat aesthetic, only used to lift interactive elements like product cards or sticky navigation bars off the background.

## Shapes

Corners use moderate rounding (`{rounded.md}` and `{rounded.lg}`) to feel friendly, modern, and accessible, softening the potentially aggressive nature of a red-heavy color palette.

## Components

Key e-commerce components are styled to encourage interaction and focus on the product:

- **Primary Button (`button-primary`):** Uses the vibrant tertiary red to draw attention to actions like "Checkout" or "Add to Cart". It features a moderate border radius for approachability.
- **Product Card (`product-card`):** Uses a pure white surface to contrast cleanly against the slightly warm background, framing product photography perfectly.

## Do's and Don'ts

- **Do** use the Tertiary color exclusively for primary actions (buying, confirming) to train the user's eye.
- **Do** rely on whitespace and the Neutral background to prevent the red tones from becoming overwhelming.
- **Don't** use pure black (`#000000`); always use the Primary color (`#2C1011`) to keep the palette harmonious.
- **Don't** use multiple bright colors that compete with the Tertiary action red. Let the red be the star.
