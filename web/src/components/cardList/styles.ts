import styled from 'styled-components'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-sizing: border-box;
  border-radius: 0.75rem;
  border: 1px solid ${({ theme }) => theme.colors.toolbar};
  background-color: ${({ theme }) => theme.colors.input};
  gap: 0.75rem;
  width: 26rem;
  height: 5.75rem;
  padding: 0.75rem 1rem;
`
export const Row = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`

export const Title = styled.h3`
  font-size: 1rem;
`

export const Options = styled.div`
  font-size: 1.5rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
`

export const Avatars = styled.div`
  display: flex;
  align-items: center;
`

export const Avatar = styled.img`
  width: 1.875rem;
  height: 1.875rem;
  border-radius: 50%;
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  margin-left: -0.5rem;
  &:first-child {
    margin-left: 0;
  }
`

export const MoreAvatars = styled.div`
  width: 1.875rem;
  height: 1.875rem;
  border-radius: 50%;
  border: 0.063rem solid ${({ theme }) => theme.colors.secondary};
  background: ${({ theme }) => theme.colors.toolbar};
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  font-weight: bold;
  color: #333;
  margin-left: -0.5rem;
`

export const LastUpdate = styled.span`
  font-size: 0.75rem;
  color: ${({ theme }) => theme.colors.lastUpdateText};
`
