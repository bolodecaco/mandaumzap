'use client'

import { Title, Wrapper } from './styles'

interface HeaderProps {
  pageTitle: string
}

export const Header = ({ pageTitle }: HeaderProps) => (
  <Wrapper>
    <Title>{pageTitle}</Title>
  </Wrapper>
)
