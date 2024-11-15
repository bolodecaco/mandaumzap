import { THEME } from '@/lib/styled/theme'
import 'styled-components'

declare module 'styled-components' {
  type Theme = typeof THEME
  // eslint-disable-next-line @typescript-eslint/no-empty-object-type
  export interface DefaultTheme extends Theme {}
}
