import styled from 'styled-components'

export const Container = styled.div`
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

export const Th = styled.div`
  text-align: left;
  padding: 10px;
  font-weight: bold;
  color: #6b7280;
`

const TdBase = styled.div`
  display: flex;
  align-items: center;
  justify-content: start;
  padding: 0.5rem 0;
  text-align: left;
`

export const Session = styled(TdBase)`
  max-width: 100%;
  width: 24rem;
`

export const UserDiv = styled.div`
  display: flex;
  width: 20rem;
  max-height: 2rem;
  flex-direction: row;
  align-items: center;
  gap: 0.5rem;
`

export const Checkbox = styled.input.attrs({ type: 'checkbox' })``
