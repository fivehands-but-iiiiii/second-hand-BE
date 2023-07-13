const colors = {
  white: '#FFFFFF',
  gray50: '#FAFAFA',
  gray100: 'rgba(249, 249, 249, 0.8)',
  gray200: 'rgba(245, 245, 245, 0.7)',
  gray300: 'rgba(179, 179, 179, 0.12)',
  gray400: 'rgba(118, 118, 128, 0.12)',
  gray500: 'rgba(179, 179, 179, 0.39)',
  gray600: 'rgba(0, 0, 0, 0.2)',
  gray700: 'rgba(60, 60, 67, 0.36)',
  gray800: 'rgba(60, 60, 67, 0.6)',
  gray900: '#3C3C43',
  black: '#000000',
  mint: '#00C7BE',
  orange: '#FF9500',
  blue: '#007AFF',
  red: '#FF3B30',
};

const palette = {
  neutral: {
    text: colors.gray900,
    textWeak: colors.gray800,
    textStrong: colors.black,
    background: colors.white,
    backgroundWeak: colors.gray50,
    backgroundBold: colors.gray400,
    backgroundBlur: colors.gray100,
    border: colors.gray500,
    borderStrong: colors.gray700,
    overlay: colors.gray600,
  },
  accent: {
    text: colors.white,
    textWeak: colors.black,
    backgroundPrimary: colors.orange,
    backgroundSecondary: colors.mint,
  },
  system: {
    default: colors.blue,
    warning: colors.red,
    background: colors.white,
    backgroundWeak: colors.gray200,
  },
};

export default palette;
