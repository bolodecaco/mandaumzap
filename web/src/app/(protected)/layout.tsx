'use client'

import { Container } from '@/components/container'
import { Header } from '@/components/header'
import { Main } from '@/lib/styled/global'
import { usePathname } from 'next/navigation'

const HEADER_MAP = {
  '/dashboard': 'Dashboard',
  '/history': 'Meu histórico',
  '/contacts': 'Meu Whatsapp',
  '/routine': 'Minha rotinas',
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
