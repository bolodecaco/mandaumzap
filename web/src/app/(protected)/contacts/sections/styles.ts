import { Button } from '@/components/button'
import { Main } from '@/lib/styled/global'
import styled from 'styled-components'

export const StyledMain = styled(Main)`
  flex-direction: row;
`
export const Delete = styled(Button)`
  color: ${({ theme }) => theme.colors.tertiary};
`

export const ListHeader = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1rem;
  border-bottom: 1px solid #e5e7eb;
`

export const List = styled.div`
  max-height: 100%;
  height: 100%;
  overflow-y: auto;
`

export const LoaderContainer = styled.div`
  max-width: 100%;
  max-height: 100%;
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`

export const Spinner = styled.div`
  width: 30px;
  height: 30px;
  border: 3px solid ${({ theme }) => theme.colors.primary};
  border-top: 3px solid transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
`

export const Session = styled.div`
  display: flex;
  align-items: center;
  justify-content: start;
  padding: 0.5rem 0;
  text-align: left;
  max-width: 100%;
  width: 24rem;
`

export const UserDiv = styled.div`
  display: flex;
  width: 20rem;
  flex-direction: row;
  align-items: center;
`
