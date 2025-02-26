'use client'

import { StyledProvider } from '@/lib/styled'
import { client } from '@/lib/tanstack'
import { QueryClientProvider } from '@tanstack/react-query'
import { SessionProvider } from 'next-auth/react'
import { NuqsAdapter } from 'nuqs/adapters/next'
import { ReactNode } from 'react'
import { ToastContainer } from 'react-toastify'

export const Registry = ({ children }: { children: ReactNode }) => (
  <NuqsAdapter>
    <QueryClientProvider client={client}>
      <SessionProvider>
        <StyledProvider>
          {children}
          <ToastContainer position="bottom-right" />
        </StyledProvider>
      </SessionProvider>
    </QueryClientProvider>
  </NuqsAdapter>
)
