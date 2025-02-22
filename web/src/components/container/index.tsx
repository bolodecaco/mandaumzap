import { ReactNode } from 'react'
import { BiHome } from 'react-icons/bi'
import { PiClockCounterClockwise } from 'react-icons/pi'
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
