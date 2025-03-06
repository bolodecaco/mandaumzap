import styled from 'styled-components'

export const Card = styled.div`
  background: ${({ theme }) => theme.variants.button.primary.color};
  border-radius: 12px;
  border: 1px solid ${({ theme }) => theme.colors.toolbar};
  padding: 0.75rem;
  width: 12.5rem;
  max-height: 11.625rem;
  gap: 1rem;
  display: flex;
  flex-direction: column;
`

export const IconWrapper = styled.div<{ active: boolean }>`
  background: ${({ active, theme }) =>
    active ? theme.colors.primary : theme.colors.toolbar};
  width: 2rem;
  height: 2rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
`

export const Title = styled.h3`
  margin: 0;
  font-size: 1rem;
  color: ${({ theme }) => theme.colors.title};
  font-weight: 500;
`

export const Info = styled.div`
  display: flex;
  align-items: center;
  gap: 6px;
  color: ${({ theme }) => theme.colors.refreshIcon};
  font-size: 14px;
`

export const Footer = styled.div`
  font-size: 10px;
  color: ${({ theme }) => theme.colors.refreshIcon};
`
