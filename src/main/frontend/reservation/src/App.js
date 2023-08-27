/* eslint-disable */
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import logo from './logo.svg';
import './App.css';

function App() {
  let [글목록, 글목록변경] = useState([]);
  let [좋아요, 좋아요변경] = useState(0);

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
  return (
    <div className="App">
      <div className='black-nav'>
        {/* 변경 자주 안되니까 걍둠 */}
        <div style={styles}>Reservation</div>
      </div>
    
      {글목록.map((글, index) => (
        <div className='list' key={index}>
          <h3> {글.name} <span onClick={() => { 좋아요변경(좋아요 + 1) }}>👍</span> {좋아요} </h3>
          <p>전화번호: {글.phoneNum}</p>
          <p>평균 평점: {글.averageRating}</p>
          <p>리뷰 수: {글.totalReviewCount}</p>
          <p>좋아요 수: {글.favoriteCount}</p>
          <hr />
        </div>
      ))}
      <Modal />
    </div>
  );
}

function Modal() {
  return (
    <>
      <div className='modal'>
        <h2>제목</h2>
        <p>날짜</p>
        <p>상세내용</p>
      </div>
    </>
  )
}

export default App;