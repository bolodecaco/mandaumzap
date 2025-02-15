import { MdOutlineRefresh } from 'react-icons/md'
import QRCode from 'react-qr-code'
import { QRCodeContainer, Refresh, RefreshQRCode } from './styles'

interface QRCodeDisplayProps {
  qrCode: string | null
  onRefresh: () => void
}

export const QRCodeDisplay = ({ qrCode, onRefresh }: QRCodeDisplayProps) => (
  <QRCodeContainer>
    <QRCode value={qrCode || ''} size={316} />
    {!qrCode && (
      <RefreshQRCode>
        <Refresh
          text="Recarregar"
          leftIcon={MdOutlineRefresh}
          iconSize={32}
          onClick={onRefresh}
          variant="primary"
        />
      </RefreshQRCode>
    )}
  </QRCodeContainer>
)
