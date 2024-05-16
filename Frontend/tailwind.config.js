/** @type {import('tailwindcss').Config} */
import daisyui from 'daisyui';

export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        OD_PURPLE: '#A93BFF',
        OD_GREEN: '#27E92A',
        OD_SKYBLUE: '#40E5FF',
        OD_YELLOW: '#FBFF42',
        OD_ORANGE: '#FF9928',
        OD_SCARLET: '#F05C4A',
        OD_JORDYBLUE: '#8FB1E1',
        OD_PINK: '#FF70A3',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: 0 },
          '100%': { opacity: 1 },
        },
      },
    },
    animation: {
      fadeIn: 'fadeIn 1s ease-out',
    },
  },
  plugins: [daisyui],
  daisyui: {
    themes: ['light'],
  },
};
