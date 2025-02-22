export const THEME = {
  colors: {
    primary: '#075E54',
    primaryForeground: '#0D8B7D',
    secondary: '#ADBBCC',
    secondaryForeground: '#2093FF',
    tertiary: '#CF2B2B',
    title: '#37475B',
    background: '#F6F8F8',
    border: '#F0F1F2',
    darkBorder: '#BDBEBF',
    input: '#FDFDFD',
    toolbar: '#EAEAEA',
    placeholder: '#A6A6A6',
    keybind: '#D3DCE5',
  },

  variants: {
    plan: {
      premium: {
        color: '#9747FF',
        'background-color': '#F9F1FF',
        'border-color': '#9747FF',
      },
      free: {
        color: '#28B0BA',
        'background-color': '#F1FEFF',
        'border-color': '#28B0BA',
      },
    },
    button: {
      primary: {
        color: '#FFFFFF',
        backgroundColor: '#075E54',
        borderColor: '#0D8B7D',
        hover: {
          backgroundColor: '#0A7669',
        },
        active: {
          backgroundColor: '#07584F',
        },
      },
      ghost: {
        backgroundColor: 'transparent',
      },
    },
  },
}
