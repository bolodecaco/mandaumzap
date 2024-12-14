'use client'

import { usePathname } from 'next/navigation'
import { ElementType } from 'react'
import { Footer, FooterItem, Logo, Nav, NavItem, Wrapper, WrapperNavs } from './styles'


interface SidebarItem {
  id: number
  href: string
  icon: ElementType
}

interface FooterItemType {
  id: number
  href: string
  icon: ElementType
}

interface SidebarProps {
  navItems: SidebarItem[]
  footerItems: FooterItemType[]
  logoProps?: {
    src: string
    alt: string
  }
}

export const Sidebar = ({ 
  navItems, 
  footerItems, 
  logoProps
}: SidebarProps) => {
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
          {footerItems.map(({ id, href, icon: Icon }, index) => (
            <FooterItem 
              key={id} 
              href={href}
              $active={index !== footerItems.length - 1 && pathname === href}
              $isLogout={index === footerItems.length - 1}
            >
              <Icon size={24} />
            </FooterItem>
          ))}
        </Footer>
      </WrapperNavs>
    </Wrapper>
  )
}