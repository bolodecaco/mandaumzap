'use client'

import { usePathname } from 'next/navigation'
import { Header } from '@/components/header'
import { Container } from '@/components/container'
import { Main } from '@/lib/styled/global'

const HEADER_MAP = {
  '/dashboard': 'Dashboard',
  '/history': 'Meu histórico',
  '/contacts': 'Meu Whatsapp',
}

export default function ProtectedLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const pathname = usePathname()

  return (
    <Container>
      <Main>
        <Header pageTitle={HEADER_MAP[pathname as keyof typeof HEADER_MAP]} />
        {children}
      </Main>
    </Container>
  )
}
