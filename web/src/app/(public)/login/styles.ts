import { Page } from '@/lib/styled/global'
import styled from 'styled-components'

export const StyledPage = styled(Page)`
  background-color: white;
  position: relative;
  align-items: center;
  padding-left: 6.5rem;
`

export const ImageContainer = styled.div`
  max-height: 37.5rem;
  max-width: 40rem;
  width: 100%;
  height: 100%;
  position: absolute;
  display: flex;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
`
export const Callout = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  max-width: 33rem;
  width: 100%;
`

export const Title = styled.h1`
  color: black;
  font-size: 4rem;
  letter-spacing: 1.25px;
  line-height: 4rem;
  font-weight: 500;
  margin-bottom: 0.5rem;
`

export const Subtitle = styled.h6`
  color: ${({ theme }) => theme.colors.title};
  font-size: 1.125rem;
`

export const Footer = styled.footer`
  position: absolute;
  box-sizing: border-box;
  bottom: 0;
  left: 0;
  right: 0;
  padding-block: 1rem;
  height: 3rem;
  border: 1px solid ${({ theme }) => theme.colors.border};
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
`
