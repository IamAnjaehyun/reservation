/* eslint-disable */
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import logo from './logo.svg';
import './App.css';

function App() {
  let [글목록, 글목록변경] = useState([]);
  let [좋아요, 좋아요변경] = useState(0);
  const [상점선택, 상점변경] = useState(null); // State to store the selected name

  useEffect(() => {
    // 네트워크 요청을 보내어 JSON 데이터 받아오기
    axios.get('http://localhost:8080/v1/guest/store')
      .then(response => {
        // 받아온 데이터의 content 배열의 name 값만 추출하여 글제목에 설정
        글목록변경(response.data.body.storeList.content);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
  }, []); // 빈 배열을 두 번째 인자로 전달하여 최초 렌더링 시에만 호출되도록 설정
  let styles = { color: 'gray', fontSize: '30px' };
  const 선택한상점변경 = (name) => {
    상점변경(name);
  };

  const 모달닫기 = () => {
    상점변경(null); // Clear the selected 글 when closing the modal
  };



  return (
    <div className="App">
      <div className='black-nav'>
        {/* 변경 자주 안되니까 걍둠 */}
        <div style={styles}>Reservation</div>
      </div>
    
      {글목록.map((글, index) => (
        <div className='list' key={index}>
          <h3 onClick={() => 선택한상점변경(글.name)}> {글.name} <span onClick={() => { 
            글목록변경(prevState => {
              const 새목록 = [...prevState];
              새목록[index] = { ...새목록[index], favoriteCount: 새목록[index].favoriteCount + 1 };
              return 새목록;
            });
          }}>👍</span> {글.favoriteCount}</h3>
          <p>전화번호: {글.phoneNum}</p>
          <p>거리: {글.description}</p>
          <p>평균 평점: {글.averageRating}</p>
          <p>리뷰 수: {글.totalReviewCount}</p>
          <hr />
        </div>
      ))}
      <Modal 선택한상점={상점선택} 글목록={글목록} 모달닫기={모달닫기} />
    </div>
  );
}

function Modal({ 선택한상점, 글목록, 모달닫기 }) {
  // 선택한 상점의 이름을 이용하여 해당 상점 정보 찾기
  const selectedStore = 글목록.find(store => store.name === 선택한상점);

  return (
    <div className='modal'>
      {selectedStore ? (
        <>
          <h2>선택한 상점: {selectedStore.name}</h2>
          <p>전화번호: {selectedStore.phoneNum}</p>
          <p>거리: {selectedStore.description}</p>
          <p>평균 평점: {selectedStore.averageRating}</p>
          <p>리뷰 수: {selectedStore.totalReviewCount}</p>
        </>
      ) : (
        <p>상세보기를 원하는 상점의 제목을 클릭해주세요.</p>
      )}
      <button onClick={모달닫기}>닫기</button>
    </div>
  );
}

export default App;





