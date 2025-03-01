import { ReactNode } from 'react'
import { BiCalendar, BiHome } from 'react-icons/bi'
import { PiAddressBook, PiClockCounterClockwise } from 'react-icons/pi'

import { Sidebar } from '../sideBar'
import { StyledPage } from './styles'

interface ContainerProps {
  children: ReactNode
}

const NAV_ITEMS = [
  {
    id: 1,
    href: '/dashboard',
    icon: BiHome,
  },
  {
    id: 2,
    href: '/routine',
    icon: BiCalendar,
  },
  {
    id: 3,
    href: '/contacts',
    icon: PiAddressBook,
  },
  {
    id: 4,
    href: '/history',
    icon: PiClockCounterClockwise,
  },
]

export const Container = ({ children }: ContainerProps) => {
  return (
    <StyledPage>
      <Sidebar navItems={NAV_ITEMS} />
      {children}
    </StyledPage>
  )
}
