/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'custom-red': '#FF6B6B',
        'custom-red-light':'#ffbcbc',
        'custom-red-dark':'#4e2222',
        'custom-blue': '#344966',
        'custom-white': '#FFF1E6',
        'custom-cyan': '#2EC4B6',
        'custom-cyan-light':'#82ede0',
        'custom-cyan-dark':'#12776f'
      }
    },
  },
  plugins: [],
}

