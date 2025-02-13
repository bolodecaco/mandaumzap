'use client'

import { StyledProvider } from '@/lib/styled'
import { client } from '@/lib/tanstack'
import { QueryClientProvider } from '@tanstack/react-query'
import { SessionProvider } from 'next-auth/react'
import { ReactNode } from 'react'

export const Registry = ({ children }: { children: ReactNode }) => (
  <QueryClientProvider client={client}>
    <SessionProvider>
      <StyledProvider>{children}</StyledProvider>
    </SessionProvider>
  </QueryClientProvider>
)
