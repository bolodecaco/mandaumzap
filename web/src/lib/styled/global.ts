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

export const Row = styled.div`
  display: flex;
  flex-direction: row;
  gap: 1rem;
  width: 100%;
`

export const Main = styled.main`
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 100%;
  gap: 1rem;
`

export const Title = styled.h2`
  font-size: 1.25rem;
  font-weight: 600;
  letter-spacing: 0.05px;
  color: ${({ theme }) => theme.colors.title};
`

export const Wrapper = styled.div`
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  padding-block: 1rem;
  padding-inline: 1.5rem;
  gap: 1rem;
  background-color: white;
  border-radius: 0.75rem;
  border: 1px solid ${({ theme }) => theme.colors.border};
`
