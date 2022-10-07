import { css, Theme } from '@emotion/react';

const closeModalButtonStyle = css`
  position: absolute;
  top: 6rem;
  right: 6rem;

  font-size: 6rem;
`;

const deleteButtonStyle = ({ colors }: Theme) => css`
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 2px ${colors.GRAY_400};

  background: ${colors.RED_400};

  font-size: 3rem;
  color: ${colors.WHITE};
`;

const headerStyle = css`
  font-size: 6rem;
`;

const layoutStyle = ({ flex, colors }: Theme) => css`
  ${flex.column};

  justify-content: space-between;
  gap: 10rem;
  overflow: overlay;
  position: relative;

  max-height: 100vh;
  padding: 12.5rem;
  border-radius: 12px;

  background: ${colors.WHITE};
`;

const renameButtonStyle = ({ colors }: Theme) => css`
  height: 8rem;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 2px ${colors.GRAY_400};

  background: ${colors.YELLOW_500};

  font-size: 3rem;
  color: ${colors.WHITE};
`;

const renameFieldSetStyle = {
  div: css`
    width: 40%;
  `,
  input: css`
    height: 8rem;
  `,
};

const renameFormStyle = ({ flex }: Theme) => css`
  ${flex.row}

  align-items: flex-start;
  justify-content: space-between;
  gap: 20rem;

  width: 100%;
`;

const sectionStyle = css`
  width: 100%;
`;

const spaceBetweenStyle = ({ flex }: Theme) => css`
  ${flex.row};

  align-items: flex-start;
  justify-content: space-between;
  gap: 20rem;

  width: 100%;
`;

const titleStyle = css`
  margin-bottom: 3rem;

  font-size: 4rem;
  font-weight: 700;
`;

export {
  closeModalButtonStyle,
  deleteButtonStyle,
  headerStyle,
  layoutStyle,
  renameButtonStyle,
  renameFieldSetStyle,
  renameFormStyle,
  sectionStyle,
  spaceBetweenStyle,
  titleStyle,
};
