import { styled } from 'styled-components'

export const Container = styled.div`
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  border-radius: 0.75rem;
  overflow: hidden;
  border: 1px solid ${({ theme }) => theme.colors.border};
  background-color: ${({ theme }) => theme.colors.input};

  display: flex;
  flex-direction: column;

  &:focus-within {
    outline-width: 0.15rem;
    outline-color: ${({ theme }) => theme.colors.primaryForeground};
    outline-style: solid;
  }
`

export const TextArea = styled.textarea`
  box-sizing: border-box;
  font-family: 'Inter', sans-serif;
  background-color: ${({ theme }) => theme.colors.input};
  font-size: 1rem;
  padding: 0.75rem;
  border-radius: 0.5rem;
  resize: none;
  width: 100%;
  height: 100%;
  border: none;

  &:focus {
    outline: none;
  }
`
export const ImageContainer = styled.div`
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-top: 10px;
`

export const ImagePreview = styled.div`
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 5px;
  overflow: hidden;
  border: 1px solid #ccc;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`

export const RemoveButton = styled.button`
  position: absolute;
  top: 5px;
  right: 5px;
  background: red;
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 12px;
  cursor: pointer;
`
