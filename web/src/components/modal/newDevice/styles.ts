import { Button } from '@/components/button'
import { styled } from 'styled-components'

export const Description = styled.p`
  font-size: 1rem;
  font-weight: 400;
  color: black;
  max-width: 20rem;
  text-align: center;
`
export const ProgressContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  align-items: center;
  justify-content: center;
  width: 100%;
`
export const Cancel = styled(Button)`
  width: 100%;
  color: ${({ theme }) => theme.colors.tertiary};
  border: 1px solid ${({ theme }) => theme.colors.tertiary};

  &:hover {
    background-color: ${({ theme }) => theme.colors.tertiary};
    color: ${({ theme }) => theme.colors.background};
  }
`

export const QRCodeContainer = styled.div`
  position: relative;
`

export const RefreshQRCode = styled.div`
  position: absolute;
  inset: 0;
  background-color: rgba(255, 255, 255, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
`

export const Refresh = styled(Button)`
  flex-direction: column;
  background-color: ${({ theme }) => theme.colors.primaryForeground};
  width: 8rem;
  height: 8rem;
  border-radius: 100%;
  font-size: 1.125rem;
`
