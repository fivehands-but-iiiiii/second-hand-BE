import { styled } from 'styled-components';

const Spinner = () => {
  return (
    <MySpinner className="spinner">
      <div></div>
      <div></div>
      <div></div>
      <div></div>
    </MySpinner>
  );
};

const MySpinner = styled.div`
  display: inline-block;
  position: relative;
  width: 64px;
  height: 64px;
  > div {
    display: block;
    position: absolute;
    width: 51px;
    height: 51px;
    margin: 6px;
    border: 5px solid #fff;
    border-radius: 50%;
    animation: spin 1s ease-in-out infinite;
    border-color: #ff9500 transparent transparent transparent;
  }
  :nth-child(1) {
    animation-delay: -0.45s;
  }
  :nth-child(2) {
    animation-delay: -0.3s;
  }
  :nth-child(3) {
    animation-delay: -0.15s;
  }
  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
`;

export default Spinner;
