import styled from 'styled-components'

export const Container = styled.div`
  display: flex;
  padding-inline: 0.25rem;
  padding-bottom: 0.75rem;
  flex-direction: column;
  gap: 0.5rem;
  border-bottom: 1px solid ${({ theme }) => theme.colors.darkBorder};
`

export const Title = styled.p`
  color: black;
  font-size: 1.125rem;
  font-weight: 500;
`

export const Description = styled.p`
  color: ${({ theme }) => theme.colors.lastUpdateText};
  line-height: 1.25;
  font-size: 0.875rem;
  font-weight: 500;
`

export const Badge = styled.div`
  background-color: ${({ theme }) => theme.colors.secondaryForeground};

  border-radius: 0.5rem;
  color: white;
  padding: 0.375rem 0.25rem;
`
