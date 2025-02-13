'use client'

import styled from 'styled-components'

export const Page = styled.main`
  box-sizing: border-box;
  display: flex;
  height: 100vh;
  max-height: 100vh;
  overflow: hidden;
  padding: 1.5rem 1rem;
  background-color: ${({ theme }) => theme.colors.background};
`

export const Main = styled.main`
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 100%;
  gap: 1rem;
`
