'use client'

import { Header } from '@/components/header'
import { Sidebar } from '@/components/sideBar'
import { useSessions } from '@/services/useSession'
import { signOut, useSession } from 'next-auth/react'
import { BiHome } from 'react-icons/bi'
import { Main, StyledPage } from './styles'

const NAV_ITEMS = [
  {
    id: 1,
    href: '/dashboard',
    icon: BiHome,
  },
]

export function Content() {
  const { data, error, isLoading } = useSessions()
  const { data: session } = useSession()
  console.log(data, error, isLoading, session)
  return (
    <StyledPage>
      <Sidebar navItems={NAV_ITEMS} footerItems={[]} />
      <Main>
        <Header pageTitle="Dashboard" />
        <button onClick={() => signOut()}>Logout</button>
      </Main>
    </StyledPage>
  )
}
