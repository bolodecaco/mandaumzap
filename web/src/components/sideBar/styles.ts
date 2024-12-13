import { css, styled } from 'styled-components'

export const Wrapper = styled.div`
  border-radius: 0.75rem;
  ${({ theme }) => css`
    background-color: ${theme.colors.background};
    border: 1px solid ${theme.colors.border};
  `}
  padding: 1.5rem 1rem;
  height: 100%;
  max-width: 4rem;
  display: flex;
  flex-direction: column;
`
export const Logo = styled.img`
  width: 2rem;
  height: 2rem;
  margin-bottom: 2rem;
  border-radius: 0.5rem;
`

export const Nav = styled.nav`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1.5rem;
`

export const NavItem = styled.a<{$active?: Boolean}>`
  width: 1.5rem;
  height: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: ${({ theme, $active }) => 
    $active ? theme.colors.primary : theme.colors.secondary};
`

export const Footer = styled.footer`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1.5rem;
`
export const FooterItem = styled.a<{ 
  $active?: boolean, 
  $isLogout?: boolean 
}>`
  color: ${({ theme, $isLogout, $active }) => 
    $isLogout 
      ? theme.colors.tertiary 
      : ($active ? theme.colors.primary : theme.colors.secondary)
  };
  width: 1.5rem;
  height: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
`
export const WrapperNavs = styled.div`
  display: flex;
  height: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
`