import styled from 'styled-components'

export const Container = styled.tr`
  display: flex;
  flex-direction: row;
  width: 100%;
  gap: 1rem;
  align-items: center;
  padding: 0.5rem 0;
  &:hover {
    background-color: #f3f4f6;
  }
`

export const Th = styled.th`
  text-align: left;
  padding: 10px;
  font-weight: bold;
  color: #6b7280;
`

const TdBase = styled.td`
  display: flex;
  align-items: center;
  justify-content: start;
  padding: 0.5rem 0;
  text-align: left;
`

export const Phone = styled(TdBase)`
  width: 12rem;
`

export const ListName = styled(TdBase)`
  max-width: 100%;
`

export const UserDiv = styled.div`
  display: flex;
  width: 19.125rem;
  flex-direction: row;
  align-items: center;
  gap: 0.25rem;
`

export const Checkbox = styled.input.attrs({ type: 'checkbox' })``

export const Avatar = styled.img`
  width: 1.875rem;
  height: 1.875rem;
  border-radius: 50%;
  border: 1px solid ${({ theme }) => theme.colors.secondary};
`
