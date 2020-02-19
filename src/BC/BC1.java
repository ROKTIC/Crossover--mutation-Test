package BC;
import java.util.*;

public class BC1 {
	static int population = 10;
	static int time = 0;
	static boolean crossoverstate = false; // crossover 했는 지 체크하는 변수, 안했으면 미래세대 변형 안하게
	static boolean end = false;
	static double fit = 0.0; //적합도 총합
	static int [][]arrayGA = new int[10][10];
	static int [][]arrayGA1 = new int[10][10];
	static int []final1 = {0,1,0,1,0,1,0,1,0,1};
	static int []final2 = {1,0,1,0,1,0,1,0,1,0};
	static double []fitness = new double[10]; // 적합도를 저장해놓는 배열
	static Random rand = new Random();
	//적합도 비율 구해서, 가장 좋은 2개 선택하고 , 크로스오버 & 돌연변이 / 새로운 유전자들 다시 넣어주고, 적합도 10 될때까지
	public static void main(String[] args) { // 메인함수
		 
		System.out.println("부모유전자 : ");
		for(int i = 0; i < 10; i++) {  // (부모) 1세대 생성
			for(int j = 0; j < 10; j++) {
				arrayGA[i][j] = rand.nextInt(2); // 0 => B, 1 => C
				System.out.print(arrayGA[i][j] + " ");
			}
			System.out.println("\n");
		}
		arraycopy(arrayGA,arrayGA1); 
		/*
		check(arrayGA);
		if(end == 111) System.out.println("유전자 일치"); // 종료검사
		else System.out.println("불일치");
		*/
		//int c = 0;
		while(end == false){
			int []bestfitness = new int[2]; // 적합도 비교로 뽑힌 최고 유전자들의 인덱스 순서
			int []worstfitness = new int[2];
			bestfitness[0] = 0; 
			bestfitness[1] = 0;
			worstfitness[0] = 0;
			worstfitness[1] = 0;
			time ++; // 횟수 누적
			
			for(int i = 0; i < 10; i++) { //적합도 계산하기
				int a = 0;
				int b = 0;
					for(int j = 0; j < 10; j++) {
						if(arrayGA[i][j] == final1[j]) a++;
						if(arrayGA[i][j] == final2[j]) b++;
							System.out.print(arrayGA[i][j] + " ");
						}
							if(a >= b) fitness[i] = a; 
							else fitness[i] = b;
							fit = fit + fitness[i];
							System.out.println("적합도 = " + fitness[i]);
			}
			
			System.out.println("현재 횟수 : " + time);
			selectBest(fitness,bestfitness);
			selectWorst(fitness,worstfitness);
			System.out.println("적합 1 : " + bestfitness[0] + " 적합 2 = " + bestfitness[1]);
			System.out.println("부적합 1 : " + worstfitness[0] + " 부적합 2 = " + worstfitness[1]);
			
			crossover(bestfitness,worstfitness);
			
			mutation();
			
			arraycopy(arrayGA,arrayGA1); 
			
			System.out.println("");
			for(int i = 0; i < 10; i++) {
				if(fitness[i] == 10.0) {
					 System.out.println("유전자 일치하였습니다." + time + "번 수행하였습니다.");
					end = true;
				}
			}
			
		} //while end
	} // main end
	
////////////////////////////////////////////////////////////// Function /////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public static int check(int [][]array,int time) { // 종료조건 함수
		
		int count = 0;
		int count1 = 0;
		for(int i = 0; i < 10; i++) {  
			for(int j = 0; j < 10; j++) {
				
				if(array[i][j] == final1[j]) count = count + 1;
				else count = 0;
				}

		}
		for(int i = 0; i < 10; i++) {  
			for(int j = 0; j < 10; j++) {
			
				if(array[i][j] == final2[j]) count1 = count1 + 1; 
				else count1 = 0;	
				}
		
		}
		
		if(count == 10 || count1 == 10) {
				System.out.println("유전자 일치하였습니다." + time + "번 수행하였습니다.");
				return 11;
		}
		return 00;
	}
	public static int[] selectBest(double []array, int []fit) { // 적합도 최고 2개 고르기
		
			for(int i = 0; i < 10; i++) {
					if((int)array[i] > array[fit[0]]) fit[0] = i;// 첫번째 최대적합도일때 인덱스 순서를 넣음
			}
			System.out.println("");
			if(fit[0] == 0) fit[1] = 1;
			for(int j = 0; j < 10; j++)
			{
				  
				   if(j != fit[0] && (int)array[j] > array[fit[1]]) fit[1] = j; // 두번째 최대 적합도일때 인덱스 순서 ( 중복 가능 )
			}
			return fit;
	}
	public static int[] selectWorst(double []array, int []fit) { // 적합도 최악 2개 고르기

		for(int i = 0; i < 10; i++) {
				if((int)array[i] < array[fit[0]]) fit[0] = i;// 첫번째 최소적합도일때 인덱스 순서를 넣음
				else continue;
		}
		if(fit[0] == 0) fit[1] = 1;
		for(int j = 0; j < 10; j++)
		{
			   if(j != fit[0] && (int)array[j] <= array[fit[1]]) fit[1] = j; // 두번째 최소 적합도일때 인덱스 순서 ( 중복 가능 )
			   else continue;
		}
		//System.out.println(fit[0] + " " + fit[1]);
		return fit;
	}
	static void crossover(int []bestfitness,int []worstfitness) {
		int temp;
		int crosspoint;
	
		if(rand.nextInt(10) > 6) return; // 0.7확률로 크로스오버 시키기
		
		System.out.println("크로스오버 함");
		crosspoint = rand.nextInt(10); // 크로스 오버할 자릿수 정하기 ( 0 - 9 )
		
		for(int i = crosspoint; i < 10; i++) { // 크로스오버 실행
			temp = arrayGA1[bestfitness[0]][crosspoint];
			arrayGA1[bestfitness[0]][crosspoint] = arrayGA1[bestfitness[1]][crosspoint];
			arrayGA1[bestfitness[1]][crosspoint] = temp;
		}
		for(int i = 0; i < 10; i++) { // worst들과 자리교체
		arrayGA[worstfitness[0]][i] = arrayGA1[bestfitness[0]][i];
		arrayGA[worstfitness[1]][i] = arrayGA1[bestfitness[1]][i];
				
		}
	} // crossover end
	
	static void mutation() { // 돌연변이
			if(time % 52 == 0) { //
				int mutpoint = rand.nextInt(10);
				int mutpoint2  = rand.nextInt(10);
				if(arrayGA[mutpoint][mutpoint2] == 0) arrayGA[mutpoint][mutpoint2] = 1;
				else arrayGA[mutpoint][mutpoint2] = 0;
				System.out.println("뮤테이션 발생");
			}
	}
	
	public static void arraycopy(int [][]array,int [][]copyarray) {
		for(int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, copyarray[i],0,array[i].length);
		}
	}

} //BC1 end

