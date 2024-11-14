'use client'

import { ReactNode } from 'react'
import { ThemeProvider } from 'styled-components'
import { Reset } from 'styled-reset'
import { StyledComponentsRegistry } from './registry'
import { THEME } from './theme'

export const StyledProvider = ({ children }: { children: ReactNode }) => (
  <StyledComponentsRegistry>
    <ThemeProvider theme={THEME}>
      <Reset />
      {children}
    </ThemeProvider>
  </StyledComponentsRegistry>
)
