'use client'

import { usePathname } from 'next/navigation'
import { ElementType } from 'react'
import { Footer, Logo, Nav, NavItem, Wrapper, WrapperNavs } from './styles'
import { Logout } from '../logout'

interface SidebarItem {
  id: number
  href: string
  icon: ElementType
}
interface SidebarProps {
  navItems: SidebarItem[]
  logoProps?: {
    src: string
    alt: string
  }
}

export const Sidebar = ({ navItems, logoProps }: SidebarProps) => {
  const pathname = usePathname()

  return (
    <Wrapper>
      {logoProps && <Logo {...logoProps} />}
      <WrapperNavs>
        <Nav>
          {navItems.map(({ id, href, icon: Icon }) => (
            <NavItem
              key={id}
              href={href}
              $active={pathname === href || (href === '/' && pathname === '/')}
            >
              <Icon size={24} />
            </NavItem>
          ))}
        </Nav>
        <Footer>
          <Logout />
        </Footer>
      </WrapperNavs>
    </Wrapper>
  )
}
