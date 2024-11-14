import { THEME } from '@/lib/styled/theme'
import 'styled-components'

type CustomTheme = typeof THEME

declare module 'styled-components' {
  export type DefaultTheme = CustomTheme
}
