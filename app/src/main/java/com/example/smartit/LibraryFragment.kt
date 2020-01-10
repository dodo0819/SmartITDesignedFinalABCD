package com.example.smartit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartit.databinding.FragmentLibraryBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_library.*


/**
 * A simple [Fragment] subclass.
 */
class LibraryFragment : Fragment(), OnItemClickListner {
    lateinit var binding: FragmentLibraryBinding
    lateinit var booklist: ArrayList<Books>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_library, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        booklist = ArrayList()

        addBooks()

        Recycler.layoutManager = LinearLayoutManager(activity)
        Recycler.addItemDecoration(DividerItemDecoration(activity,1))
        Recycler.adapter = Adapter(booklist, this)
    }

    fun addBooks(){
        booklist.add(Books("Foundations of Computer Science","This text book was published in 1992, and has since been taken out of print. This is what the authors say about this book: “We believed in 1992 it was the way to introduce theory in Computer Science, and we believe that today.","Chapter1  Computer Science\nLink :http://infolab.stanford.edu/~ullman/focs/ch01.pdf \n\nChapter2  Iteration, Induction, and Recursion\nLink :http://infolab.stanford.edu/~ullman/focs/ch02.pdf \n\nChapter3  The Running Time of Programs\nLink : http://infolab.stanford.edu/~ullman/focs/ch03.pdf \n\nChapter4  The Running Time of Programs\nLink :http://infolab.stanford.edu/~ullman/focs/ch04.pdf\n\nChapter5  The Tree Data Model\nLink :http://infolab.stanford.edu/~ullman/focs/ch05.pdf\n\n",R.drawable.book1) )
        booklist.add(Books("Introduction to Computing","This book covers essential computer science concepts. It uses Python and Scheme, but it’s not really a book about those two programming languages.","Chapter1  Computing\nLink :http://computingbook.org/Computing.pdf \n\nChapter2  Language\nLink :http://computingbook.org/Language.pdf \n\nChapter3  Programming\nLink : http:http://computingbook.org/Programming.pdf \n\nChapter4  Problems and Procedures\nLink :http://computingbook.org/Problems.pdf\n\nChapter5  Data\nLink :http://computingbook.org/Data.pdf\n\n", R.drawable.book2) )
        booklist.add(Books("The Architecture of Open Source Applications","This is a trilogy of books about the design/architecture of open source software. From the description: “If you are a junior developer, and want to learn how your more experienced colleagues think, these books are the place to start.”","Chapter1  Blockcode: A visual programming toolkit\nLink :http://www.aosabook.org/en/500L/blockcode-a-visual-programming-toolkit.html \n\nChapter2  A Continuous Integration System\nLink :http://www.aosabook.org/en/500L/a-continuous-integration-system.html \n\nChapter3  Clustering by Consensus\nLink : http://www.aosabook.org/en/500L/clustering-by-consensus.html \n\nChapter4  Contingent: A Fully Dynamic Build System\nLink :http://www.aosabook.org/en/500L/contingent-a-fully-dynamic-build-system.html\n\nChapter5  A Web Crawler With asyncio Coroutines\nLink :http://www.aosabook.org/en/500L/a-web-crawler-with-asyncio-coroutines.html\n\n", R.drawable.book3) )
        booklist.add(Books("Foundations of Programming","This book is based on an article series called “Foundations of Programming” by software developer Karl Seguin. The topics — which are written from a .NET developer’s perspective — are about things all programmers care about. Examples: unit testing, memory allocation, and DRY code.", "Link :https://www.openmymind.net/FoundationsOfProgramming.pdf",R.drawable.book4) )

    }

    override fun onItemClick(item: Books, position: Int) {

        val intent = Intent(activity, DetailsActivity::class.java)
        intent.putExtra("NAME", item.name)
        intent.putExtra("DESC", item.description)
        intent.putExtra("LOGO", item.logo.toString())
        intent.putExtra("LINK", item.link.toString())
        startActivity(intent)


    }


}
