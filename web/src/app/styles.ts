'use client'
import { styled } from 'styled-components'

export const Back = styled.div`
  box-sizing: border-box;
  display: flex;
  height: 5rem;
  overflow: hidden;
  padding: 1.5rem 1rem;
  background-color: ${({ theme }) => theme.colors.background};
`
